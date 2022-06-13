import React, { useEffect, useState } from 'react';
import { requestGet } from '@/services/request';
import { urlAdminCount } from '@/services/url';
import { createMessageLoading } from '@/utils/message';
import { Scene } from '@antv/l7';
import { CountryLayer } from '@antv/l7-district';
import { Mapbox } from '@antv/l7-maps';
import styles from '@/pages/admin/analyze/analyze.scss';

interface CountResponse {
  provinceVOList: MapData[];
}

interface MapData {
  name: string;
  code: number;
  value: number;
}

const AdminAnalyzePage: React.FC = () => {
  const colors = ['#E9F4FA', '#CCDCEF', '#8CBEDF', '#4191C2', '#125CA3'];

  const queryCount = async () => {
    const scene = new Scene({
      id: 'map',
      map: new Mapbox({
        center: [116.2825, 39.9],
        pitch: 0,
        style: 'blank',
        zoom: 3,
        minZoom: 0,
        maxZoom: 10
      })
    });

    const loading = createMessageLoading('admin_count_loading');
    try {
      await loading.showLoading('获取数据中');
      const [res, ok] = await requestGet<null, CountResponse>(urlAdminCount);
      if (ok.succeed && res) {
        loading.success('获取成功');
        const layer = new CountryLayer(scene, {
          data: res.provinceVOList,
          joinBy: ['NAME_CHN', 'name'],
          depth: 1,
          provinceStroke: '#fff',
          cityStroke: '#EBCCB4',
          cityStrokeWidth: 1,
          fill: {
            color: {
              field: 'value',
              values: colors
            }
          },
          popup: {
            enable: true,
            Html: (props: any) => {
              return `<span>${props.NAME_CHN} : ${props.value}人</span>`;
            }
          }
        });
        scene.on('load', () => layer);
      } else {
        loading.error('获取失败');
      }
    } catch (e) {
      loading.error('获取失败');
    }
  };

  useEffect(() => {
    queryCount();
  }, []);

  return (
    <div className={styles.background}>
      <div className={styles.content}>
        <div className={styles.title}>实习省份分布</div>
        <div className={styles.map} id={'map'} />
      </div>
    </div>
  );
};

export default AdminAnalyzePage;
