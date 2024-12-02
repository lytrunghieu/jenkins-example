pipeline {
    agent any
    environment {
        NETLIFY_AUTH_TOKEN = credentials('NETLIFY_AUTH_TOKEN_CREDENTIALS_ID')
        NETLIFY_SITE_ID = credentials('NETLIFY_SITE_ID_CREDENTIALS_ID')
        APP_CENTER_API_KEY = credentials('APP_CENTER_API_KEY')
        APP_CENTER_USER = credentials('APP_CENTER_USER')
        HEROKU_API_KEY = credentials('HEROKU_API_KEY')
    }
    stages {
        stage('Clear ws before') {
            steps {
                cleanWs()
                
            }
        }
        stage('Preparing'){
            steps {
                 script {
                    REPO_NAME = params.REPO_NAME
                    REPO_HOST = params.REPO_HOST
                    ONLY_AUTOTEST = params.ONLY_AUTOTEST
                    
                    BUILD_WEB =  params.PLATFORMS.contains('WEB')
                    BUILD_WEB_SUCCESS = false
                    
                    BUILD_SERVER =  params.PLATFORMS.contains('SERVER')
                    SERVER_DEPLOY_SCRIPT_PATH_FULL = "${REPO_NAME}/${params.SERVER_DEPLOY_SCRIPT_PATH}"

                    BUILD_ANDROID_MOBILE = params.PLATFORMS.contains('ANDROID_MOBILE')
                    BUILD_ANDROID_MOBILE_SUCCESS = false
                    
                    ANDROID_MOBILE_BUILD_PATH = params.ANDROID_MOBILE_BUILD_PATH
                    ANDROID_APP_NAME_APP_CENTER = params.ANDROID_APP_NAME_APP_CENTER
                    DISTRIBUTION_GROUP_APP_CENTER = params.DISTRIBUTION_GROUP_APP_CENTER
                    APP_DEPLOY_SCRIPT_PATH = params.APP_DEPLOY_SCRIPT_PATH
                    WEB_DEPLOY_SCRIPT_PATH = params.WEB_DEPLOY_SCRIPT_PATH
                    
                    WEB_BUILD_PATH = params.WEB_BUILD_PATH
                    WEB_TEST_SCRIPT_PATH_FULL = "${REPO_NAME}/${params.WEB_TEST_SCRIPT_PATH}"
                    WEB_TEST_RESULT_FILE_PATH_FULL = "${params.REPO_NAME}/web/${params.WEB_TEST_RESULT_PATH}"
                    WEB_TEST_ARTIFACTS_DIR_PATH = "${params.REPO_NAME}/web/artifacts/web/screenshots"
                    
                    ANDROID_TEST_SCRIPT_PATH = "${REPO_NAME}/${params.ANDROID_TEST_SCRIPT_PATH}"
                    ANDROID_TEST_RESULT_FILE_PATH_FULL = "${params.REPO_NAME}/mobileapp/${params.ANDROID_TEST_RESULT_PATH}"
                    ANDROID_TEST_ARTIFACTS_DIR_PATH = "${REPO_NAME}/mobileapp/android/artifacts/android.emu.debug*/*"
                    
                }
            }
        }
        stage('Clone repository') {
            steps {
                script {
                    sh """git clone --no-tags --depth 100 ${REPO_HOST}/${REPO_NAME}.git"""
                }
                
            }
            
        }
        stage('Build web') {
            when { expression { currentBuild.result != 'ABORTED' && BUILD_WEB && ONLY_AUTOTEST == false } }
            steps {
                script {
                     sh """
                     cd ./${REPO_NAME}/web && yarn && yarn build
                    """
                    BUILD_WEB_SUCCESS = true
                }
            }
        }
        stage('Deploy web to Netlify') {
            when { expression { currentBuild.result != 'ABORTED' && BUILD_WEB_SUCCESS } }
            steps {
                script {
                     sh """
                     cd ./${REPO_NAME} && ./${WEB_DEPLOY_SCRIPT_PATH} $NETLIFY_AUTH_TOKEN $NETLIFY_SITE_ID
                    """
                }
            }
        }
        stage('Deploy Server to Heroku') {
            when { expression { currentBuild.result != 'ABORTED' && BUILD_SERVER } }
            steps {
                script {
                    sh """
                        cd ./${REPO_NAME} && ./${params.SERVER_DEPLOY_SCRIPT_PATH} $HEROKU_APP_NAME $HEROKU_API_KEY
                    """
                }
            }
        }
        stage('Build Android Mobile') {
            when { expression { currentBuild.result != 'ABORTED' && BUILD_ANDROID_MOBILE && ONLY_AUTOTEST == false } }
            steps {
                script {
                     sh """
                     cd ./${REPO_NAME}/mobileapp && yarn && yarn build_android
                    """
                    BUILD_ANDROID_MOBILE_SUCCESS = true
                }
            }
        }
        stage('Deploy android mobile to App Center') {
            when { expression { currentBuild.result != 'ABORTED' && BUILD_ANDROID_MOBILE_SUCCESS } }
            steps {
                script {
                     sh """
                     ./${REPO_NAME}/${APP_DEPLOY_SCRIPT_PATH} ${ANDROID_APP_NAME_APP_CENTER} ${APP_CENTER_USER} ${REPO_NAME}/${ANDROID_MOBILE_BUILD_PATH} ${DISTRIBUTION_GROUP_APP_CENTER} ${APP_CENTER_API_KEY}
                    """
                }
            }
        }
         stage('Testing android mobile') {
            when { expression { currentBuild.result != 'ABORTED' &&  BUILD_ANDROID_MOBILE && ONLY_AUTOTEST == true } }
            steps {
                script {
                     sh """
                     ./${ANDROID_TEST_SCRIPT_PATH}
                    """
                }
            }
        }
        stage('Testing web') {
            when { expression { currentBuild.result != 'ABORTED' &&  BUILD_WEB && ONLY_AUTOTEST == true } }
            steps {
                script {
                     sh """
                     ./${WEB_TEST_SCRIPT_PATH_FULL}
                    """
                }
            }
        }
    }
    post {
        success {
           script {
                // Archive screenshots for failed test
                archive(includes: "${ANDROID_TEST_ARTIFACTS_DIR_PATH}")
                archive(includes: "${WEB_TEST_ARTIFACTS_DIR_PATH}")
                
                sh """
                    mkdir -p reports/combined
                    if [ -f ${ANDROID_TEST_RESULT_FILE_PATH_FULL} ]; then 
                        cat ${ANDROID_TEST_RESULT_FILE_PATH_FULL} >> reports/combined/android.html
                    else 
                        echo "Android test result file not found at ${ANDROID_TEST_RESULT_FILE_PATH_FULL}."
                    fi
                    if [ -f ${WEB_TEST_RESULT_FILE_PATH_FULL} ]; then 
                        cat ${WEB_TEST_RESULT_FILE_PATH_FULL} >> reports/combined/web.html
                    else 
                        echo "Web test result found at ${WEB_TEST_RESULT_FILE_PATH_FULL}."
                    fi
                """
                
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,reportDir: 'reports/combined',
                    reportFiles: 'web.html,android.html',
                    reportName: 'Test Results'
                ])

            }
        }
        always {
            script{
                slackSend channel: "${params.SLACK_CHANNEL}", color: "warning", message: "Build #${env.BUILD_NUMBER} finished with status: ${currentBuild.currentResult}"
            }
        }
    }

}