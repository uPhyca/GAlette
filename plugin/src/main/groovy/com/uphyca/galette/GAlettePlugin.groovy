package com.uphyca.galette

import org.gradle.api.GradleException
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class GAlettePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        final def variants
        def plugin
        if (project.plugins.hasPlugin(AppPlugin)) {
            variants = project.android.applicationVariants
            plugin = project.plugins.getPlugin(AppPlugin)
        } else if (project.plugins.hasPlugin(LibraryPlugin)) {
            variants = project.android.libraryVariants
            plugin = project.plugins.getPlugin(LibraryPlugin)
        } else {
            throw new GradleException("The 'android' or 'android-library' plugin is required.")
        }

        project.configurations.create('galette')

        def analyticsVersion = '9.2.1'
        def galetteVersion = getPluginVersion(project, "com.uphyca.galette", "galette-plugin")
        def agpVersion = getPluginVersion(project, "com.android.tools.build", "gradle")
        def agpMajorVersion = Integer.parseInt(agpVersion.split("\\.")[0])

        if (5 <= agpMajorVersion) {
            project.dependencies {
                implementation "com.google.android.gms:play-services-analytics:${analyticsVersion}"
                implementation "com.uphyca.galette:galette:${galetteVersion}"
                galette "com.uphyca.galette:galette-instrumentation:${galetteVersion}"
            }
        } else {
            // 以前のバージョンとの互換のため compile を使用
            project.dependencies {
                compile "com.google.android.gms:play-services-analytics:${analyticsVersion}"
                compile "com.uphyca.galette:galette:${galetteVersion}"
                galette "com.uphyca.galette:galette-instrumentation:${galetteVersion}"
            }
        }

        variants.all { variant ->
            JavaCompile javaCompile
            if (variant.hasProperty('javaCompileProvider')) {
                javaCompile = variant.javaCompileProvider.get()
            } else if (variant.hasProperty('javaCompiler')) {
                javaCompile = variant.javaCompiler
            } else {
                javaCompile = variant.javaCompile
            }
            javaCompile.doLast {
                def classpath = project.files()

                if (project.android.properties['bootClasspath']) {
                    // 0.10
                    project.android.bootClasspath.each { classpath += project.files(it) }
                } else if (plugin.properties['runtimeJarList']) {
                    // 0.9
                    plugin.runtimeJarList.each { classpath += project.files(it) }
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
