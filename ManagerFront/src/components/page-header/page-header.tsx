import React, { CSSProperties } from 'react';
import styles from './page-header.scss';

interface Props {
  Icon: React.ForwardRefExoticComponent<any>;
  headContent: string;
  style?: CSSProperties;
}

/**
 * 每一个功能页面的标题封装
 * @param props
 */
const Index: React.FC<Props> = ({ Icon, headContent, style }) => {
  return (
    <>
      <div className={styles.container} style={style}>
        <div className={styles.wrapper}>
          <Icon />
        </div>
        <div>{headContent}</div>
      </div>
    </>
  );
};

export default Index;
