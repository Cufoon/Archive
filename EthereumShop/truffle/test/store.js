const ContractStore = artifacts.require("Store");

contract("Store", (accounts) => {
  it("should put 10000 MetaCoin in the first account", async () => {
    const storeInstance = await ContractStore.deployed();
    const balance = await storeInstance.getBalance.call(accounts[0]);

    assert.equal(balance.valueOf(), 10000, "10000 wasn't in the first account");
  });
});
