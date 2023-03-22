import { Space } from '@arco-design/web-react';
import { IconCopyright } from '@arco-design/web-react/icon';
import styles from './index.scss';

const Footer: React.FC = () => {
  return (
    <div className={styles.footer}>
      <div className={styles.links}>
        <Space size='medium'>密码学与区块链期末答辩项目</Space>
      </div>
      <div className={styles.copyright}>
        Copyright <IconCopyright /> Cufoon
      </div>
    </div>
  );
};

export default Footer;
