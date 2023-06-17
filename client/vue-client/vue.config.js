module.exports = {
  lintOnSave: true,
  devServer:{
    // host:"localhost",
    // public:"http://localhost:9001",
    proxy:{
      "^/h5p":{
        target: "http://localhost:8081",
        changeOrigin: true
      }
    },
    
  }
};
