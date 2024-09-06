# OrbitHub Platform-SpringBoot

This project is a sophisticated social media network platform, developed with the SpringBoot framework and SSM. It is specifically engineered to offer exceptional throughput, security, and responsive user experiences.

![Screenshot 2024-03-02 at 16 58 29](https://github.com/Wilson-ZheLin/Algorithm-Community-Network-SpringBoot/assets/145169519/075a219a-0aae-4823-a007-7e213d8ba92e)

### Technology Stack:
* **Framework & Architecture:** SpringBoot, Spring, Spring MVC
* **Data Persistence:** MyBatis, MySQL
* **Caching:** Redis, Caffeine
* **Message Queuing:** Kafka
* **Search Engine:** Elasticsearch
* **Security & Authentication:** MD5 Encryption, Spring Security, Kaptcha
* **Data Statistics:** Redis HyperLogLog, Bitmap
* **Multi-Thread Scheduling:** Spring Quartz, Redis
* **Logging & Exception Handling:** Spring AOP

Features
--------

* **User Authentication:** Secure password with **salting** and **MD5** encryption, **Kaptcha** for captcha, and Spring Email for activation

* **Multi-Level Caching:** Adopted a **multi-level cache** form of local cache (`Caffeine`) + second-level cache (`Redis`) + database

* **Event Handling with Kafka:** `Kafka` integration for **asynchronous system notifications** and frequently accessed features

* **Automated Post Scoring:** Integrated `Quartz` for periodic post scoring updates and hot post ranking

* **Access Control:** Spring Security and custom Interceptors to manage user privacy and prevent unauthorized access

* **Elasticsearch-Powered Search:** Advanced search functionality with keyword highlighting, leveraging `Elasticsearch`

* **Asynchronous Posting:** **Prefix tree-based** sensitive word filtering, enhancing user experience and content quality

* **Redis for Data Analytics:** Utilizing `Redis` HyperLogLog and Bitmap for accurate, space-efficient UV and DAU analytics

* **Exception Handling & Logging:** Global exception handling and Spring AOP for comprehensive service layer logging

Demo
----
To be updated..
