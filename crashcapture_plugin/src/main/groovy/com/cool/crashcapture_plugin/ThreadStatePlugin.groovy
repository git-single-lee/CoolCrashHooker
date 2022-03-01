package com.cool.crashcapture_plugin

import com.android.build.api.transform.Transform;
import com.android.build.gradle.AppExtension // be careful
import com.android.build.gradle.AppPlugin
import com.cool.crashcapture_plugin.extension.ThreadStateExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * author : coolqi.li
 * e-mail : coolqi.li@forever.com
 * date   : 2021/8/20-3:38 下午
 * desc   :
 * version: 1.0
 */
public class ThreadStatePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        try {
            println("crouter===> begin CoolRouterPlugin, apply from ${project.name}")
            // just in is app module.
            if (project.plugins.hasPlugin(AppPlugin)) {
                AppExtension appExtension = project.extensions.getByType(AppExtension)
                Transform serviceTransform = new ThreadStateTransform();
                appExtension.registerTransform(serviceTransform)
            }
            if (!project.plugins.hasPlugin(AppPlugin)) {
                println("crouter===> not app module, quit.")
                return
            }
            project.extensions.create("ThreadCapture", ThreadStateExtension)
            // project evaluate over.
            project.afterEvaluate {
                println("crouter===> we do something.")
                Object extensionAble = project.extensions.findByName("ThreadCapture")
                if (extensionAble != null) {
                    ThreadStateExtension readyOk = extensionAble
                }
            }
        } catch(Exception e) {
            println("crouter===> a error: " + e.toString())
        }
    }
}
