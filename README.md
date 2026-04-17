# UI_CardActivity

Полноценный Android-проект на Kotlin + Jetpack Compose с экраном оплаты в премиальной стилистике.

## Что внутри

- `MainActivity` и точка входа приложения.
- Экран оплаты `PaymentScreen` с требуемыми слоями и анимациями.
- Разделение на composable-компоненты (`AnimatedWaves`, `AnimatedCard`, `QrPanel`, `GlassActionButton`, `TopBar` и т.д.).
- Design tokens: цвета, размеры, motion-параметры.

## Запуск

> Рекомендуется JDK 17.

```bash
JAVA_HOME=/path/to/jdk17 ./gradlew installDebug
```

Если `JAVA_HOME` не указан и используется слишком новая JDK, Gradle/AGP могут падать на этапе конфигурации.

## Шрифты Montserrat (локально, без коммита бинарников)

Для точного соответствия макету добавьте локально файлы:

- `app/src/main/res/font/montserrat_regular.ttf`
- `app/src/main/res/font/montserrat_medium.ttf`
- `app/src/main/res/font/montserrat_semibold.ttf`
- `app/src/main/res/font/montserrat_bold.ttf`

В текущем PR бинарные ассеты намеренно исключены, чтобы PR создавался без ошибки `Бинарные файлы не поддерживаются`.
