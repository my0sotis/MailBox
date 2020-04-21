**1**. 因为现在还没有与后台数据对接，receive  draft  al_send  rubbishMail 四个界面获取的邮件信息全部都是项目static文件夹下的本地json文件，而且代码中的url用的是绝对路径，端口号不同的要修改一下

后期会直接从后台读取数据，不需要读取本地文件，目前只是为了测试一下json

**2**. 后台传递的数据格式可以参考static内的json格式  sender和receiver的区别  注意，**是一个json数组，其中包含多个object，不是一个object**

**3**. lookMail还没有完成，可以跳转，但是还不能显示数据

**4**. 项目recieveMail中的recieve拼错了，但是我这部分的代码涉及到的还是按照recieve来做，如果后台有用到这个界面，注意一下拼写问题

**5**. 其中的axios交互部分，**后台怎么写可以看一下** 主要参考csdn的这篇博客 https://learner.blog.csdn.net/article/details/88955387  











# mailtext2

> A Vue.js project

## Build Setup

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report

# run unit tests
npm run unit

# run e2e tests
npm run e2e

# run all tests
npm test
```

For a detailed explanation on how things work, check out the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).

