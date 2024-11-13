/** @type {import('@jest/types').Config.InitialOptions} */
module.exports = {
  rootDir: '..',
  testMatch: ['<rootDir>/e2e/**/*.test.js'],
  testTimeout: 120000,
  maxWorkers: 1,
  globalSetup: 'detox/runners/jest/globalSetup',
  globalTeardown: 'detox/runners/jest/globalTeardown',
  reporters: ['detox/runners/jest/reporter'],
  testEnvironment: 'detox/runners/jest/testEnvironment',
  verbose: true,
  reporters: ['detox/runners/jest/reporter', [
    'jest-html-reporters',
    { publicPath: './reports',
      filename: 'jest_html_reporters.html',
      expand: true,
      inlineSource: true,
    },
  ]],
};
