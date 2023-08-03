package io.alinturbut

import org.gradle.api.Plugin
import org.gradle.api.Project

open class NyxPluginExtension()

class NyxPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        project.pluginManager.apply("com.mooltiverse.oss.nyx")
        tasks.register("configureNyx") {
            project.extensions.configure<com.mooltiverse.oss.nyx.gradle.NyxExtension>("nyx") {
                preset.set("simple")
                git {
                    remotes {
                        create("origin") {
                            authenticationMethod.set("USER_PASSWORD")
                            user.set("{{#environmentVariable}}GH_TOKEN{{/environmentVariable}}")
                            password.set("{{#environmentVariable}}GH_TOKEN{{/environmentVariable}}")
                        }
                    }
                }
                changelog {
                    path.set("CHANGELOG.md")
                }
                initialVersion.set("1.0.0")
                releaseTypes {
                    enabled.addAll("mainline", "internal")
                    publicationServices.addAll("github")
                    remoteRepositories.addAll("origin")
                    items {
                        create("mainline") {
                            collapseVersions.set(false)
                            filterTags.set("^({{configuration.releasePrefix}})?([0-9]\\d*)\\.([0-9]\\d*)\\.([0-9]\\d*)$")
                            gitCommit.set("true")
                            gitCommitMessage.set("Release version {{version}}")
                            gitPush.set("true")
                            gitTag.set("true")
                            gitTagMessage.set("Tag version {{version}}")
                            matchBranches.set("^(master|main)$")
                            matchEnvironmentVariables.set(mapOf("CI" to "^true$"))
                            matchWorkspaceStatus.set("CLEAN")
                            publish.set("true")
                            versionRangeFromBranchName.set(false)
                        }
                        create("internal") {
                            collapseVersions.set(true)
                            collapsedVersionQualifier.set("internal")
                            description.set("Internal release {{version}}")
                            gitCommit.set("false")
                            gitPush.set("false")
                            gitTag.set("false")
                            publish.set("false")
                            versionRangeFromBranchName.set(false)
                        }
                    }
                }
                services {
                    create("github") {
                        type.set("GITHUB")
                        geOptions().putAll(mapOf(
                                "AUTHENTICATION_TOKEN" to "{{#environmentVariable}}GH_TOKEN{{/environmentVariable}}",
                                "REPOSITORY_NAME" to "nyx-gradle-plugin-configuration",
                                "REPOSITORY_OWNER" to "alinturbut"))
                    }
                }
                summaryFile.set(".nyx-summary.txt")
                stateFile.set(".nyx-state.json")
                verbosity.set("DEBUG")
            }
        }
    }
}
