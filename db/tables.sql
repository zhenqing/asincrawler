/* crawl_log */
CREATE TABLE IF NOT EXISTS crawl_log (
  keyword VARCHAR(100) PRIMARY KEY NOT NULL,
  lastCrawlTime DATETIME NOT NULL
);