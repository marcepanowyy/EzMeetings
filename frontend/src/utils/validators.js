export const emailValidator = (email) => /\S+@\S+\.\S+/.test(email);
export const passwordValidator = (password) => {
    return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/.test(password);
  };
  

