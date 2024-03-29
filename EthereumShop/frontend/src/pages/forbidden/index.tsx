import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './index.scss';

const Forbidden: React.FC = () => {
  const navigate = useNavigate();

  useEffect(() => {
    setTimeout(() => {
      navigate('/');
    }, 1500);
  }, []);

  return (
    <div className={styles.outer}>
      <div className={styles.png}></div>
      <div className={styles.text}>你来到了不存在的页面</div>
      <div className={styles.hint}>1.5秒后回到首页</div>
    </div>
  );
};

export default Forbidden;
