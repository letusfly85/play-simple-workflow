# Simple Dynamic Workflow engine with Playframework 2.6.X

##

```bash
mkdir -p $HOME/work/docker/mysql/simple_workflow

docker run --name simple_workflow \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=simple_workflow \
  -v $HOME/work/docker/mysql/simple_workflow:/var/lib/mysql \
  -p 3308:3306 \
  -d mysql \
  mysqld \
  --datadir=/var/lib/mysql \
  --user=mysql \
  --server-id=1 \
  --log-bin=/var/log/mysql/mysql-bin.log \
  --binlog_do_db=example \
  --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```

## Entity Relational Diagram

- coming soon...

