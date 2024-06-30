# Android-Step-Counter
이 프로젝트는 기기 사용자의 걸음 수를 추적하는 방법을 소개하는 예시 프로젝트입니다.<br/>
10분 안에 이해하여 따라할 수 있을 정도로 쉽고 간편합니다.<br/><br/>

## Why is this the best way
Android 버전이 높아지면서 구글은 배터리, 기기 성능, 보안 등을 더 엄격하게 관리하고 있습니다.<br/>
특히 배터리를 낭비할 수 있는 백그라운드 작업을 제한하고 있습니다.<br/>

- *[몇 가지 특수한 사례를 제외하고 앱이 백그라운드에서 실행되는 동안 포그라운드 서비스를 시작할 수 없습니다.](https://developer.android.com/develop/background-work/services/foreground-services?hl=ko#bg-access-restrictions)*<br/>

- *[많은 사용 사례에서 포그라운드 서비스를 사용하는 데 사용할 수 있는 API가 있습니다.<br/>적합한 용도로 빌드된 API가 있는 경우 거의 항상 포그라운드 서비스 대신 이 API를 사용해야 합니다](https://developer.android.com/develop/background-work/services/foreground-services?hl=ko#purpose-built-apis)*<br/>

- [가속도계](https://source.android.com/docs/core/interaction/sensors/sensor-types?hl=ko#accelerometer), [걸음 감지기](https://source.android.com/docs/core/interaction/sensors/sensor-types?hl=ko#step_detector), [걸음수 측정기](https://source.android.com/docs/core/interaction/sensors/sensor-types?hl=ko#step_counter)는 모두 백그라운드에서는 동작하지 않는 [non-wake-up 센서](https://source.android.com/docs/core/interaction/sensors/suspend-mode?hl=ko#non-wake-up_sensors)입니다.<br/><br/>

## How it works
1. 포그라운드에서는 센서를 사용합니다.<br/>
2. 백그라운드에는 [Recording Api](https://developer.android.com/health-and-fitness/guides/recording-api?hl=ko)를 사용합니다.<br/><br/>

## Special advantages
- Recording Api는 구글 로그인을 필요로 하지 않습니다.<br/>
구글 로그인 과정에서 발생하는 이탈을 방지하세요.<br/>

- 절전 모드, 인터넷 연결, 주기적인 백그라운드 작업, Google Play 정책, 리부팅 등 모든 복잡한 요소를 고려하지 않아도 됩니다.<br/>
쉽게 구현하고, 쉽게 사용하세요.<br/><br/>
