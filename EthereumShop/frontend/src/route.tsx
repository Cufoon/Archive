import React from 'react';
import { Route, Routes } from 'react-router-dom';
import NoPage from '$pages/forbidden';
import Home from '$pages/home';
import PushProduct from '$pages/product-publish';
import MineProduct from '$pages/product-mine';
import StoreHome from '$pages/shop-home';
import StoreRank from '$pages/shop-rank';
import App from './app';

const RouteList: React.FC = () => {
  return (
    <Routes>
      <Route path='/' element={<App />}>
        <Route index element={<Home />} />
        <Route path='product-publish' element={<PushProduct />} />
        <Route path='product-mine' element={<MineProduct />} />
        <Route path='shop-home' element={<StoreHome />} />
        <Route path='shop-rank' element={<StoreRank />} />
      </Route>
      <Route path='forbidden' element={<NoPage />} />
      <Route path='*' element={<NoPage />} />
    </Routes>
  );
};

export default RouteList;
