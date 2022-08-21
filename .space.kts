/**
* JetBrains Space Automation
* This Kotlin-script file lets you automate build activities
* For more info, see https://www.jetbrains.com/help/space/automation.html
*/

job("Publish package on new version tag") {
	startOn {
        gitPush {
            branchFilter {
                +"refs/heads/master"
            }

            tagFilter {
                +"snapshot-*"
                +"rc-*"
                +"release-*"
            }
        }
    }

    gradle(null, "build")
    gradle(null, "publish")
}
