module.exports = {
    browsers: ['chromium'],
    exitOnPageError: false, // GitHub currently throws errors
    launchOptions: {
        headless: false,
        devtools: false,
        executablePath: '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',
        args: ['--new-instance', '--no-first-run', '--no-default-browser-check','--aggressive-cache-discard', '--disable-application-cache', '--incognito'],
    },
    contextOptions: {
      viewport: { width: 1280, height: 720 },
    },
    serverOptions: {
        command: 'yarn start',
        launchTimeout: 120000,
        port: 3000,
        protocol: 'http',
        host: '127.0.0.1',
    },
};
