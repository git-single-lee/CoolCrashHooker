package com.cool.crashcapture_plugin;

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;
import com.cool.crashcapture_plugin.collector.ThreadHacker
import com.cool.crashcapture_plugin.extension.ThreadStateExtension;

/**
 * author : coolqi.li
 * e-mail : coolqi.li@forever.com
 * date   : 2021/8/20-3:38 下午
 * desc   :
 * version: 1.0
 */
public class ThreadStateTransform extends Transform implements ThreadHacker.IThreadHacker {
    ThreadStateExtension getThreadStateExtension() {
        return threadStateExtension
    }

    void setThreadStateExtension(ThreadStateExtension threadStateExtension) {
        this.threadStateExtension = threadStateExtension
    }

    private ThreadStateExtension threadStateExtension = new ThreadStateExtension()

    private static final String NAME = "CrashCapture";

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    void hackerCallback(File hackedFile, File to) {
        FileUtils.copyDirectory(hackedFile, to)
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        boolean isIncremental = isIncremental()
        if (!isIncremental) {
            transformInvocation.outputProvider.deleteAll();
        }
        transformInvocation.inputs.each {
            // class
            it.directoryInputs.each { directoryInput ->
                def destDir = transformInvocation.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes,
                        directoryInput.scopes, Format.DIRECTORY)
                println("capture===> show directoryInput: name: " + directoryInput.name + " contentTypes: " + directoryInput.contentTypes + " scopes: " + directoryInput.scopes + " destDir: " + destDir.toString())
                ThreadHacker.tryInjectWithClass(directoryInput, destDir, this)
            }

            it.jarInputs.each { jarInput ->
                def jarDest = transformInvocation.outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes,
                        jarInput.scopes, Format.JAR);
                println("capture===> show jarInput: name: " + jarInput.name + " contentTypes: " + jarInput.contentTypes + " scopes: " + jarInput.scopes + " jarDest: " + jarDest.toString())
                ThreadHacker.tryInjectWithJar(jarInput, jarDest)
            }
        }
    }



    @Override
    public boolean isIncremental() {
        return false;
    }
}
