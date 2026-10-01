# spring-cloud-template

Spring Cloud 微服务脚手架：作为新项目的起点模板，`Use this template` 或克隆后即可在其上开发业务模块。

## 技术栈

| 组件 | 版本 |
| --- | --- |
| Spring Boot | 2.7.18 |
| Spring Cloud | 2021.0.9 |
| Spring Cloud Alibaba | 2021.0.5.0（Nacos 注册中心 + 配置中心） |
| Spring Cloud Gateway | 网关 |
| OpenFeign | 声明式服务调用 |
| SpringDoc | 2.5.0 |
| MyBatis-Plus | 3.5.4.1 |
| Sa-Token / Shiro | 1.37.0 / 1.9.1 |
| Druid / Redisson | 1.2.16 / 3.19.3 |
| Hutool / MapStruct | 5.8.22 / 1.5.5.Final |

## 模块结构

```
spring-cloud-template
├── common                  # 通用能力（common-log：注解式操作日志 + 事件异步落库）
├── gateway       :5100     # 网关（路由、验证码、密码解码、Swagger 鉴权、访问日志过滤器）
├── iam                     # 身份与权限：iam-api（DTO/PO）+ iam-service
└── basic         :5200     # 示例业务服务：basic-api（Feign 接口 + 降级工厂）+ basic-service + basic-controller
```

## 快速开始

需要 JDK 8+ 与一个可用的 Nacos（默认 `nacos:8848`，账号密码见根 `pom.xml` 的 `nacos.username` / `nacos.password`）。

```bash
mvn -DskipTests test-compile                          # 编译
mvn -pl basic/basic-controller -am spring-boot:run    # 启动示例服务
mvn -pl gateway -am spring-boot:run                   # 启动网关
```

- 配置中心：`application.yml` 使用 Config Data Loader，从 Nacos 拉取 `application-<profile>.yml` 与 `<服务名>-<profile>.yml`
- 本地开发：`dev` profile 使用 H2 内存库，控制台 `/h2`，建表与初始化脚本位于 `basic/basic-service/src/main/resources/db/`

## 作为模板使用

1. 在 GitHub 仓库页点 **Use this template**，或 `git clone` 后删除 `.git` 重新 `git init`
2. 修改根 `pom.xml` 的 `groupId` / `artifactId` / `name`，并同步各一级模块 `<parent>` 中的 `artifactId`
3. 按需重命名 `org.zero.*` 包与二级模块（`basic` 可替换为你的业务域）
