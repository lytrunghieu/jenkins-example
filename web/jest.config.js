module.exports = {
    verbose: true,
    preset: 'jest-playwright-preset',
    transform: { '^.+\\.(ts|tsx)$': 'ts-jest' },
    testMatch: [
        '**/e2e/**.e2e.js',
        '**/e2e/**/**.e2e.js',
    ],
    reporters: [
        'default',
        ['jest-html-reporters', {
            filename: 'webreport.html',
            inlineSource: true,
        }],
    ],
    testEnvironment: './TestEnvironment.js',
    testTimeout: 30000,
    //setupFilesAfterEnv: ['jest-allure/dist/setup'],
};
