# youtube-video-extractor

Multi threaded Application to automate extraction of youtube videos for migration to website.

Given configuration in [application.properties](src/main/resources/application.properties) this application will:
- Open a website and authenticate.
- Collect video page urls.
- Iterate over the video pages and return all youtube video ids grouped by page title.
- Download all the collected videos in parallel
- Exit the application once all tasks submitted to the executor service have completed.

Uses:
- Java 14
- Selenium
- Completable future
- Executor service
- Dependency Injection via guice
- Selenium
