# javaToDo — Органайзер задач

## Docker: PostgreSQL + Adminer

### Запуск

```bash
cp .env.example .env
docker compose up -d
```

### Adminer

Откройте в браузере: http://localhost:8081

| Поле     | Значение   |
|----------|------------|
| System   | PostgreSQL |
| Server   | db         |
| Username | todo       |
| Password | todo       |
| Database | javatodo   |

> **Важно:** в Adminer поле **Server** — это имя сервиса из `docker-compose.yml` (`db`), не `localhost`.

### Остановка

```bash
docker compose down
```

Данные сохраняются в Docker volume `postgres_data`. Чтобы удалить и данные:

```bash
docker compose down -v
```

### Подключение из Java (для следующих этапов)

```
jdbc:postgresql://localhost:15432/javatodo
user: todo
password: todo
```
