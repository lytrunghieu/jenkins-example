const { chromium } = require('@playwright/test');

let browser;
let page;

beforeAll(async () => {
  browser = await chromium.launch();
  page = await browser.newPage();
});

afterAll(async () => {
  await browser.close();
});

global.page = page;
