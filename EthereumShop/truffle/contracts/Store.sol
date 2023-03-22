// SPDX-License-Identifier: MIT
pragma solidity 0.8.17;

// 代币系统
contract Token {
    uint256 tokenTotal; // 代币总和
    uint256 tokenPrice; // 代币价格
    uint256 balanceTokens; // 合约余额

    // 所有用户余额记录
    mapping(address => uint256) balances;

    event buySuccess(address addr, uint256 num);
    event sellSuccess(address addr, uint256 num);

    // 获取余额信息 [代币总和 代币价格 合约余额 合约金币 用户余额 用户金币]
    function getBalanceInfo()
        public
        view
        returns (
            uint256,
            uint256,
            uint256,
            uint256,
            uint256,
            uint256
        )
    {
        return (
            tokenTotal,
            tokenPrice,
            balanceTokens,
            address(this).balance,
            balances[msg.sender],
            msg.sender.balance
        );
    }

    // 买入代币
    function buy() public payable {
        uint256 tokensToBuy = msg.value / tokenPrice;
        require(tokensToBuy <= balanceTokens); // 合约代币是否足够
        // 更新信息
        balances[msg.sender] += tokensToBuy;
        balanceTokens -= tokensToBuy;
        emit buySuccess(msg.sender, tokensToBuy);
    }

    // 卖出代币
    function sell(uint256 tokensToSell) public {
        require(tokensToSell <= balances[msg.sender]); // 用户代币是否足够
        // 更新信息
        uint256 total = tokensToSell * tokenPrice;
        balances[msg.sender] -= tokensToSell;
        balanceTokens += tokensToSell;
        payable(msg.sender).transfer(total);
        emit sellSuccess(msg.sender, tokensToSell);
    }
}

// 商店系统
contract Store is Token {
    constructor(uint256 _tokens, uint256 _tokenPrice) {
        tokenTotal = _tokens; // 100000
        tokenPrice = _tokenPrice; // 100000000000000000
        balanceTokens = tokenTotal; // 100000
    }

    // 用户
    struct User {
        uint256[] purchasedProducts; // 已购买的商品
        uint256[] publishedProducts; // 已发布的商品
    }

    // 商品
    struct Product {
        address owner; // 所有者
        string name; // 商品名
        string style; // 类型
        string intro; // 简介
        string rules; // 玩法
        uint256 price; // 价格
        uint256 sales; // 销量
        uint256 score; // 评分
        uint256 date; // 日期
        string cover; // 封面 (ipfs)
        string file; // 文件 (ipfs)
        uint256 clen; // 评价数量
        mapping(uint256 => Comment) comments; // 评价列表
    }

    // 评价
    struct Comment {
        address buyer; // 购买者
        uint256 date; // 日期
        uint256 score; // 评分
        string content; // 评论
    }

    Product[] products;
    mapping(address => User) userPool;

    event publishSuccess(
        uint256 id,
        string name,
        string style,
        string intro,
        string rules,
        uint256 price,
        uint256 date,
        string cover,
        string file
    );
    event purchaseSuccess(uint256 id, address addr, uint256 price);
    event evaluateSuccess(uint256 id, address addr, uint256 score);

    // 获取已经购买的商品列表
    function getPurchasedProducts() public view returns (uint256[] memory) {
        return userPool[msg.sender].purchasedProducts;
    }

    // 获取已经发布的商品列表
    function getPublishedProducts() public view returns (uint256[] memory) {
        return userPool[msg.sender].publishedProducts;
    }

    // 获取商品数量
    function getProductLength() public view returns (uint256) {
        return products.length;
    }

    // 获取评价数量
    function getCommentLength(uint256 id) public view returns (uint256) {
        return products[id].clen;
    }

    // 获取商品信息
    function getProductInfo(uint256 id)
        public
        view
        returns (
            address,
            string memory,
            string memory,
            string memory,
            string memory,
            uint256,
            uint256,
            uint256,
            uint256,
            string memory,
            string memory
        )
    {
        require(id < products.length);
        // 获取商品
        Product storage g = products[id];
        return (
            g.owner,
            g.name,
            g.style,
            g.intro,
            g.rules,
            g.price,
            g.sales,
            g.score,
            g.date,
            g.cover,
            g.file
        );
    }

    // 获取商品文件
    function getProductFile(uint256 id) public view returns (string memory) {
        require(id < products.length);
        // 获取商品
        Product storage g = products[id];
        require(g.owner == msg.sender || isPurchased(id)); // 限制条件
        return (g.file);
    }

    // 获得评价信息
    function getCommentInfo(uint256 gid, uint256 cid)
        public
        view
        returns (
            address,
            uint256,
            uint256,
            string memory
        )
    {
        require(gid < products.length);
        require(cid < products[gid].clen);
        // 获取评价
        Comment storage c = products[gid].comments[cid];
        return (c.buyer, c.date, c.score, c.content);
    }

    // 是否已经购买 通过遍历实现
    function isPurchased(uint256 id) public view returns (bool) {
        User storage u = userPool[msg.sender];
        for (uint256 i = 0; i < u.purchasedProducts.length; i++)
            if (u.purchasedProducts[i] == id) return true; // 已经购买
        return false; // 尚未购买
    }

    // 是否已经评价 通过遍历实现
    function isEvaluated(uint256 id) public view returns (bool) {
        Product storage g = products[id];
        for (uint256 i = 0; i < g.clen; i++)
            if (g.comments[i].buyer == msg.sender) return true; // 已经评价
        return false; // 尚未评价
    }

    // 发布商品
    function publish(
        string memory name,
        string memory style,
        string memory intro,
        string memory rules,
        uint256 price,
        string memory cover,
        string memory file
    ) public {
        uint256 id = products.length;
        // 记录发布
        Product storage g = products.push();
        g.owner = msg.sender;
        g.name = name;
        g.style = style;
        g.intro = intro;
        g.rules = rules;
        g.price = price;
        g.sales = 0;
        g.score = 0;
        g.date = block.timestamp;
        g.cover = cover;
        g.file = file;
        g.clen = 0;
        userPool[msg.sender].publishedProducts.push(id);

        emit publishSuccess(
            id,
            name,
            style,
            intro,
            rules,
            price,
            g.date,
            cover,
            file
        );
    }

    // 购买商品
    function purchase(uint256 id) public {
        require(id < products.length);
        // 读取合约
        Product storage g = products[id];
        require(g.owner != msg.sender && !isPurchased(id)); // 限制条件
        require(balances[msg.sender] >= g.price); // 合法条件

        // 记录购买
        balances[msg.sender] -= g.price;
        balances[g.owner] += g.price;
        g.sales++;
        userPool[msg.sender].purchasedProducts.push(id);

        emit purchaseSuccess(id, msg.sender, g.price);
    }

    // 评价商品
    function evaluate(
        uint256 id,
        uint256 score,
        string memory content
    ) public {
        require(id < products.length);
        // 读取合约
        Product storage g = products[id];
        require(g.owner != msg.sender && isPurchased(id) && !isEvaluated(id)); // 限制条件
        require(0 <= score && score <= 10); // 合法条件

        // 记录评价
        g.score += score;
        g.comments[g.clen++] = Comment(
            msg.sender,
            block.timestamp,
            score,
            content
        );

        emit evaluateSuccess(id, msg.sender, g.score);
    }

    fallback() external {
        revert();
    }
}
