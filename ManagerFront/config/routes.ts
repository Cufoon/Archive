import { defineConfig } from 'umi';

const routes = defineConfig({
  routes: [
    {
      path: '/',
      component: '@/pages/home/home',
      layout: {
        hideMenu: true,
        hideNav: true,
        hideFooter: true
      }
    },
    {
      path: '/login',
      component: '@/pages/login/login',
      layout: {
        hideMenu: true,
        hideNav: true,
        hideFooter: true
      }
    },
    {
      path: '/student/archive',
      component: '@/pages/student/archive/archive',
      access: 'studentRouteFilter'
    },
    {
      path: '/student/message',
      component: '@/pages/message/message',
      access: 'studentRouteFilter'
    },
    {
      path: '/student/info',
      component: '@/pages/student/info/info',
      access: 'studentRouteFilter'
    },
    {
      path: '/student/changePassword',
      component: '@/pages/password/password',
      access: 'studentRouteFilter'
    },
    {
      path: '/student/internship',
      component: '@/pages/student/internship/internship',
      access: 'studentRouteFilter'
    },
    {
      path: '/student/choose',
      component: '@/pages/student/choose/choose',
      access: 'studentRouteFilter'
    },
    {
      path: '/student/teacher',
      component: '@/pages/student/teacher/teacher',
      access: 'studentRouteFilter'
    },
    {
      path: '/student/document',
      component: '@/pages/studentDocument/studentDocument',
      access: 'studentRouteFilter'
    },
    {
      path: '/student/document/tableReport',
      component: '@/pages/studentDocument/table/tableReport',
      access: 'studentRouteFilter'
    },
    {
      path: '/student/document/tableExam',
      component: '@/pages/studentDocument/table/tableExam',
      access: 'studentRouteFilter'
    },
    {
      path: '/student/document/tableIdentify',
      component: '@/pages/studentDocument/table/tableIdentify',
      access: 'studentRouteFilter'
    },
    {
      path: '/student/document/tableAppraisal',
      component: '@/pages/studentDocument/table/tableAppraisal',
      access: 'studentRouteFilter'
    },
    {
      path: '/student/document/tableSummary',
      component: '@/pages/studentDocument/table/tableSummary',
      access: 'studentRouteFilter'
    },
    {
      path: '/teacher/message',
      component: '@/pages/message/message',
      access: 'teacherRouteFilter'
    },
    {
      path: '/teacher/info',
      component: '@/pages/teacher/info/info',
      access: 'teacherRouteFilter'
    },
    {
      path: '/teacher/changePassword',
      component: '@/pages/password/password',
      access: 'teacherRouteFilter'
    },
    {
      path: '/teacher/myStudent',
      component: '@/pages/teacher/my-students/my-students',
      access: 'teacherRouteFilter'
    },
    {
      path: '/teacher/document',
      component: '@/pages/teacherDocument/teacherDocument',
      access: 'teacherRouteFilter'
    },
    {
      path: '/teacher/dealApply',
      component: '@/pages/teacher/apply/deal-apply',
      access: 'teacherRouteFilter'
    },
    {
      path: '/admin/changePassword',
      component: '@/pages/password/password',
      access: 'adminRouteFilter'
    },
    {
      path: '/admin/user',
      component: '@/pages/admin/user/user',
      access: 'adminRouteFilter'
    },
    {
      path: '/admin/relation',
      component: '@/pages/admin/relation/relation',
      access: 'adminRouteFilter'
    },
    {
      path: '/admin/score',
      component: '@/pages/admin/score/score',
      access: 'adminRouteFilter'
    },
    {
      path: '/admin/internship',
      component: '@/pages/admin/internship/internship',
      access: 'adminRouteFilter'
    },
    {
      path: '/admin/analyze',
      component: '@/pages/admin/analyze/analyze',
      access: 'adminRouteFilter'
    },
    {
      path: '/admin/notification',
      component: '@/pages/admin/notification/notification',
      access: 'adminRouteFilter'
    }
  ]
});

export default routes;
