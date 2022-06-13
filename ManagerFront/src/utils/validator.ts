import isEmailTool from 'validator/es/lib/isEmail';
import isPhoneTool from 'validator/es/lib/isMobilePhone';
import isStrongPasswordTool from 'validator/es/lib/isStrongPassword';

export const isEmail = (strToTest: string): boolean => {
  return isEmailTool(strToTest, {
    allow_utf8_local_part: false
  });
};

export const isPhone = (strToTest: string): boolean => {
  return isPhoneTool(strToTest, 'zh-CN');
};

const regOnlyNumberAndAlphabet = /^[0-9A-Za-z]+$/;
export const isNicePassword = (strToTest: string): [boolean, number] => {
  let isOK: boolean = false;
  let errorLevel: number = 0;
  if (strToTest.length && strToTest.length < 17 && strToTest.length > 7) {
    if (regOnlyNumberAndAlphabet.test(strToTest)) {
      if (
        isStrongPasswordTool(strToTest, {
          minLength: 8,
          minLowercase: 1,
          minUppercase: 1,
          minNumbers: 1,
          minSymbols: 0,
          returnScore: false
        })
      ) {
        isOK = true;
      } else {
        errorLevel = 1;
      }
    } else {
      errorLevel = 2;
    }
  } else {
    errorLevel = 3;
  }
  return [isOK, errorLevel];
};

export const checkEmail = (value: string) => {
  if (!isEmail(value)) {
    throw new Error('请输入正确的邮箱地址');
  }
};

export const checkPhone = (value: string) => {
  if (!isPhone(value)) {
    throw new Error('请输入正确的手机号');
  }
};
