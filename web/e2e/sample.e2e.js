

describe('Home page E2E Test', () => {
  it('should load homepage and verify the title', async () => {
    await page.goto('http://localhost:3000');
    const title = await page.title();
    expect(title).toBe('React App');
  });
});