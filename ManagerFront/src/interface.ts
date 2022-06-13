export interface InitialStateData {
  name: string;
  avatar?: string;
  /** 0: 游客, 1: 学生, 2: 指导老师, 3: 管理员 */
  role: number;
}

export interface RoleData {
  name: string;
  type: number;
  avatar: string;
}
