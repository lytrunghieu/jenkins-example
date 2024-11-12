import {device, expect, element} from 'detox';

beforeAll(async () => {
  await device.launchApp({newInstance: true});
  const platform = await device.getPlatform();
  console.log('Test running on platform: ', platform, device.name);
  console.log('Test running on device: ', device.name);
});

afterAll(async () => {
  await device.terminateApp();
  await device.uninstallApp();
});

describe('Example', () => {
  
  it('should have welcome screen', async () => {
    await expect(element(by.id('container'))).toBeVisible();
  });
});
