

@Library('global_jenkins_functions') _

import groovy.transform.Field


/*************************************************************** | PARAMETERS | ******************************************************************/


@Field JOB = [:]


JOB.git_project_url = "https://github.com/AlexeyMihaylovDev/selenoid.git"
JOB.project_name = "terraform_bot_test"
JOB.devops_sys_user = "my_polybot_key"
JOB.branch = "master"
JOB.email_recepients = "mamtata2022@gmail.com" //TODO: add all developers of projects




properties([parameters([
        [$class: 'ParameterSeparatorDefinition', name: 'HEADER', sectionHeader: 'You will be prompted for parameters during the run.'],
        [$class: 'WHideParameterDefinition', defaultValue: "", description: '', name: 'Build_Type'],
        [$class: 'WHideParameterDefinition', defaultValue: "System", description: '', name: 'JOB_EXECUTOR'],
        [$class: 'WHideParameterDefinition', defaultValue: "System", description: '', name: 'JOB_EXECUTOR_ID'],
        [$class: 'WHideParameterDefinition', defaultValue: "", description: '', name: 'Deploy']

])])

pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
        timestamps()
        ansiColor('xterm')
    }

    agent {
        docker {
            image '352708296901.dkr.ecr.eu-central-1.amazonaws.com/alexey_jenk_agent:ubuntu'
            label 'linux'
            args '--user root -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    stages {
        stage('Get & Print Job Parameters') {
            steps {
                script {
                    def cause = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')
                    println("userName: ${cause.userName}")
                    JOB.allModules.each { moduleName, moduleDetails ->
                    }
                    //"params" is immutable, so in order to modify it need to copy it to another object.
                    JOB.params = [:]
                    params.each { key, value ->
                        if (!value.equals("")) {
                            println("${key}: ${value}")
                            JOB.params["${key}"] = "${value}"
                            println(JOB.params)
                        }
                    }
                }
            }
        }
        stage('Inputs') {
            when { expression { params.Build_Type != "auto_trigger" } }
            steps {
                script {
                    println("===================================${STAGE_NAME}=============================================")
                    def userInput = input id: 'UserInput', message: 'Please provide parameters.', ok: 'OK', parameters: [
                            choice(name: 'Build_Type', choices: ['plan', 'destroy'], description: '\'DESTROY\' - Destroy framework. \'PLAN\' - Show framework.'),
                            booleanParam(description: 'Click the checkbox if you want to run terraform apply', name: 'Continue_apply'),
                            booleanParam(description: 'Continue to  Run tests', name: 'run_tests')

                    ]
                    println("-------------------------Inputs provided by user:--------------------------------")
                    JOB.Build_Type = userInput["Build_Type"]
                    JOB.apply = userInput['Continue_apply']
                    JOB.deploy = userInput['run_tests']
                    println(JOB.params.modules)
                }
            }
        }
        stage('Clone') {
            steps {
                script {

                    // Clone PolyBot repository.
                    git branch: "${JOB.branch}", url: "${JOB.git_project_url}"
                    JOB.gitCommitHash = global_gitInfo.getCommitHash(JOB.branch)
                    println("====================${JOB.gitCommitHash}==============")
                }
            }

        }
        stage("Run Terraform command ") {
            steps {
                script {
                    println("===================================${STAGE_NAME} : ${JOB.Build_Type} =============================================")
                    dir('terraform') {
                        sh 'terraform init '
                        if (JOB.Build_Type == "plan") {
                            sh "terraform plan -out=myplan.txt   "
                        } else if (JOB.Build_Type == "destroy") {
                            sh "terraform plan -out=myplan.txt   "
                            sh 'terraform destroy -auto-approve '

                        }
                    }
                }
            }
        }
        stage('Deploy framework on AWS') {
            when { expression { JOB.apply == true } }
            steps {
                script {
                    println("===================================${STAGE_NAME}=============================================")
                    dir('terraform') {
                        sh ' terraform apply "myplan.txt"'
                        sh 'terraform output -json > ./infrastructure.json'
                    }
                }
            }
        }
    }
        post {

            always {
                script {
                    currentBuild.description = ("Branch : ${JOB.branch}\n GitCommiter : ${JOB.commitAuthor}\nDeploy_server: ${JOB.apply}")

                    EMAIL_MAP = [
                            "Modules"      : JOB.params.modules,
                            "Build Type"   : JOB.params.Build_Type,
                            "Deploy To Env": JOB.params.Deploy_Environment,
                            "Job Name"     : JOB_NAME,
                            "Run deploy "  : JOB.apply,
                            "Build Number" : BUILD_NUMBER,
                            "Git tag Name" : JOB.tagName,
                            "Branch"       : "${JOB.branch}",
                            "More Info At" : "<a href=${BUILD_URL}console> Click here to view build console on Jenkins. </a>",
                            "painted"      : "false"
                    ]
                    global_sendGlobalMail.sendByMapFormat(JOB.email_recepients, currentBuild.result, EMAIL_MAP, JOB.emails,
                            "Jenkins Report", "Build Notification - Jenkins Report", "BOT build")

                }
            }
        }
    }
