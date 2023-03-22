import { InitialStateData } from './app';

export default function (initialState: InitialStateData) {
  const { role } = initialState;
  return {
    studentRouteFilter: role === 1,
    teacherRouteFilter: role === 2,
    adminRouteFilter: role === 3
  };
}
