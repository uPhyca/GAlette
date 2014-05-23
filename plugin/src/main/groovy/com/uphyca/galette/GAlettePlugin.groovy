package com.uphyca.galette

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class GAlettePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        final def variants
        final def plugin
        if (project.plugins.hasPlugin(AppPlugin)) {
            variants = project.android.applicationVariants
            plugin = project.plugins.getPlugin(AppPlugin)
        } else if (project.plugins.hasPlugin(LibraryPlugin)) {
            variants = project.android.libraryVariants
            plugin = project.plugins.getPlugin(LibraryPlugin)
        } else {
            throw new IllegalStateException("The 'android' or 'android-library' plugin is required.")
        }

        project.configurations.create('galette')

        def galetteVersion = getPluginVersion(project, "com.uphyca.galette", "galette-plugin")

        project.dependencies {
            compile 'com.google.android.gms:play-services:4.3.+@aar'
            compile "com.uphyca.galette:galette:${galetteVersion}@aar"
            galette "com.uphyca.galette:galette-instrumentation:${galetteVersion}"
        }

        variants.all { variant ->
            JavaCompile javaCompile = variant.javaCompile

            javaCompile.doLast {
                def classpath = project.files()

                if (plugin.properties['runtimeJarList']) {
                    // 0.9
                    plugin.runtimeJarList.each { classpath += project.files(it) }
                } else if (project.android.properties['bootClasspath']) {
                    // 0.10
                    project.android.bootClasspath.each { classpath += project.files(it) }
                }
                classpath += javaCompile.classpath
                classpath += project.configurations["galette"]
                classpath += project.files(javaCompile.destinationDir)

                def uris = classpath.collect {
                    it.toURI().toURL()
                }

                def loader = new URLClassLoader(uris as URL[], (ClassLoader) null)
                def instClass = loader.loadClass("com.uphyca.galette.GAletteInstrumentation")
                def inst = instClass.newInstance()
                inst.processFiles(javaCompile.destinationDir)
            }
        }
    }

    String getPluginVersion(Project project, String group, String name) {
        def Project targetProject = project
        while (targetProject != null) {
            def version
            targetProject.buildscript.configurations.classpath.resolvedConfiguration.firstLevelModuleDependencies.each {
                e ->
                    if (e.moduleGroup.equals(group) && e.moduleName.equals(name)) {
                        version = e.moduleVersion
                    }
            }
            if (version != null) {
                return version
            }
            targetProject = targetProject.parent
        }
        return null
    }
}
