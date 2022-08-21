/**
* JetBrains Space Automation
* This Kotlin-script file lets you automate build activities
* For more info, see https://www.jetbrains.com/help/space/automation.html
*/

job("Publish package on new version tag") {
	startOn {
        gitPush {
             tagFilter {
                +"snapshot-*"
                +"rc-*"
                +"release-*"
            }
        }
    }

    container("displayName = 'Run gradle build & publish', image = gradle:jdk17") {
        kotlinScript { api ->
            api.gradle("build")
            api.gradle("publish")
        }
    }
}
