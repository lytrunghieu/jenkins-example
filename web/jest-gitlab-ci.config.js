module.exports = {
    verbose: true,
    testMatch: [
        '**/__tests__/**.test.tsx',
        '**/__tests__/**/**.test.tsx',
    ],
    moduleFileExtensions: ['js', 'jsx', 'ts', 'tsx', 'android.js', 'ios.js'],
    moduleDirectories: ['node_modules', 'src'],
    transform: {
        '^.+\\.(js|jsx)$': 'babel-jest',
        '^.+\\.(ts|tsx)$': 'ts-jest',
    },
    moduleNameMapper: {
        '\\.(css|less|scss|sass)$': '<rootDir>/__mocks__/styleMock.ts',
    },
    testTimeout: 30000,
    testEnvironment: 'jsdom',
};
