# Topicos MQTT - CanTrace IoT Station

## Convencao de nomenclatura
Padrao: cantrace/<area>/<categoria>/<recurso>

- area: identificador do local (ex.: area-01)
- categoria: telemetry, status, alert, command
- recurso: temperature, humidity, light, led, buzzer

## Lista de topicos
| Topico | Direcao | QoS sugerido | Payload esperado |
| --- | --- | --- | --- |
| cantrace/area-01/telemetry/temperature | dispositivo -> broker | 0 | JSON com value, unit, mode |
| cantrace/area-01/telemetry/humidity | dispositivo -> broker | 0 | JSON com value, unit, mode |
| cantrace/area-01/telemetry/light | dispositivo -> broker | 0 | JSON com value, unit, mode |
| cantrace/area-01/status | dispositivo -> broker | 0 | JSON com status de conexao |
| cantrace/area-01/alert | dispositivo -> broker | 0 | JSON com alerta textual |
| cantrace/area-01/command/led | dashboard -> dispositivo | 1 | JSON com state ON/OFF |
| cantrace/area-01/command/buzzer | dashboard -> dispositivo | 1 | JSON com state ON/OFF ou action BEEP |

## Exemplos de payload
Temperatura:
```json
{
	"device_id": "cantrace-iot-01",
	"ts": 123456,
	"value": 25.4,
	"unit": "C",
	"mode": "simulated"
}
```

Umidade:
```json
{
	"device_id": "cantrace-iot-01",
	"ts": 123456,
	"value": 60.1,
	"unit": "pct",
	"mode": "sensor"
}
```

Luminosidade:
```json
{
	"device_id": "cantrace-iot-01",
	"ts": 123456,
	"value": 512,
	"unit": "raw",
	"mode": "sensor"
}
```

Status:
```json
{
	"device_id": "cantrace-iot-01",
	"ts": 123456,
	"wifi_rssi": -56,
	"ip": "192.168.0.50",
	"mqtt_connected": true,
	"mode": "simulated"
}
```

Comando:
```json
{
	"state": "ON",
	"source": "dashboard"
}
```

## Comandos de teste
Assinar todos os topicos:
```bash
mosquitto_sub -h 127.0.0.1 -t "cantrace/area-01/#" -v
```

Publicar comando de LED:
```bash
mosquitto_pub -h 127.0.0.1 -t "cantrace/area-01/command/led" -m "{\"state\":\"ON\",\"source\":\"cli\"}"
```

Publicar comando de buzzer:
```bash
mosquitto_pub -h 127.0.0.1 -t "cantrace/area-01/command/buzzer" -m "{\"state\":\"OFF\",\"source\":\"cli\"}"
```

Publicar telemetria de teste:
```bash
mosquitto_pub -h 127.0.0.1 -t "cantrace/area-01/telemetry/temperature" -m "{\"device_id\":\"sim\",\"ts\":1,\"value\":24.5,\"unit\":\"C\",\"mode\":\"simulated\"}"
```
