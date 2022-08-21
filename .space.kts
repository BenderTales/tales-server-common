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

    gradle("openjdk:17", "build", "publish")
}
