package com.devilwwj.plugin

import org.apache.commons.collections.Closure;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

class ApkDistPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('apkdistconf', ApkDistExtension);

        project.afterEvaluate {
            if (!project.android) {
                throw new IllegalStateException('Must apply \'com.android.application\' or \'com.android.library\' first!')
            }

            if (project.apkdistconf.nameMap == null || project.apkdistconf.destDir == null) {
                project.logger.info('Apkdist conf should be set!')
                return
            }

            Closure nameMap = project['apkdistconf'].nameMap
            String destDir = project['apkdistconf'].destDir

            project.android.applicationVariant.all { variant ->
                variant.output.each { output ->
                    File file = output.outputFile
                    output.outputFile = new File(destDir, nameMap(file.getName()))
                }
            }
        }
    }
}