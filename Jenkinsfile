pipeline {
agent any
parameters {

choice(name :'TestingEnvironment', choices :[
'STG',
'DEV',
'QA',
'UI'
],
description : "Select the Testing Environment"
)

choice(name :'UseCloudEnv', choices :[
'True',
'False'
],
description : "Use Cloud Environment"
)
choice(name :'cloudEnvName', choices :[
'browserstack',
'saucelab'
],
description : "Select the cloud Environment"
)

string(description: 'Entre the Url : ', defaultValue: 'https://www.exemple.com/', name: 'Url')

choice(description : "Select the Operating System: " ,name :'Os', choices :[
'Mac',
'Windows',
'Linux'
]
)

choice(description : "Select the OS Version:" ,name :'Os_version', choices :[
'Big Sur',
'Catalina',
'Windows 7',
'Windows 10'
]
)
choice(name :'Headless', choices :[
'False',
'True'
],
description : "Headless Browser ?"
)

choice(name :'BrowserName', choices :[
'Chrome',
'Safari',
'Fire Fox',
'Internet Explorer',
'All'
],
description : "Select the Browser"
)

string(defaultValue: '94', description: 'Browser Version', name: 'BrowserVersion')

choice(name :'tag', choices :[
'@regression',
'@sanity',
'@smoke' ,
'@orderEvent',
'@loadTesting'
],
description : "Select the test suit using the corresponding Tag"
)

string(defaultValue: '2', description: 'Implicitly wait time', name: 'ImplicitlyWaitTime')

string(defaultValue: 'testkarim1980@gmail.com', description: 'email for notifications', name: 'notification_email')

}
stages {
stage('Running tests suit'){
steps{
// echo "The testing Environment is : ${TestingEnvironment} "
// echo "The testing Browser is : ${Browsers} "
// echo "Headless Browser : ${Headless} "
// echo "The running Tag is : ${tag}"

//sh "mvn test -Dcucumber.filter.tags=${tag}"

//sh "mvn install -Dcucumber.filter.tags=${tag}"
  bat(/"mvn install -Dcucumber.filter.tags=${tag}"/)

// sh 'mvn test -Dcucumber.options=”–tags ${tag}”'
//echo "The application testing en ${TestingEnvironment} Environment, ${Browsers} Browser and Tag ${tag} was performed"
}
}
}
post {
always {
echo "Test succeeded"
emailext attachmentsPattern: '**/*.html, **/*.pdf',
body: "${env.BUILD_URL} has result: ${currentBuild.result}",
subject: "Status of pipeline: ${currentBuild.fullDisplayName}",
to: "${notification_email}"
cucumber fileIncludePattern: 'target/reports/cucumber-reports/cucumber.json', sortingMethod: 'ALPHABETICAL'
}
}
}