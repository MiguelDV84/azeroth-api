# Guía de uso de la API Azeroth

## Resumen de cambios implementados

Se ha implementado la relación muchos-a-muchos entre **Razas** y **Clases**, permitiendo que cada raza tenga un conjunto específico de clases disponibles, tal como en World of Warcraft: Wrath of the Lich King.

### Cambios realizados:

1. **Entidades actualizadas:**
   - `Raza`: Ahora incluye la lista `clasesDisponibles` (relación Many-to-Many con Clase)
   - `Clase`: Incluye la relación inversa con las razas

2. **Tabla intermedia creada:**
   - `raza_clase`: Almacena las combinaciones válidas de raza-clase

3. **Validación agregada:**
   - El servicio `JugadorService` valida que la clase elegida sea válida para la raza seleccionada

4. **Nuevos endpoints:**
   - `GET /api/razas` - Obtener todas las razas con sus clases disponibles
   - `GET /api/razas/{id}` - Obtener una raza específica con sus clases

## Combinaciones de Raza-Clase implementadas

### Alianza:

- **HUMANO**: Guerrero, Paladín, Mago, Sacerdote, Pícaro, Brujo, Caballero de la Muerte
- **ENANO**: Guerrero, Paladín, Cazador, Pícaro, Sacerdote, Caballero de la Muerte
- **GNOMO**: Guerrero, Pícaro, Mago, Brujo, Caballero de la Muerte
- **ELFO_NOCHE**: Guerrero, Cazador, Pícaro, Sacerdote, Druida, Caballero de la Muerte
- **DRAENEI**: Guerrero, Paladín, Cazador, Sacerdote, Chamán, Mago, Caballero de la Muerte

### Horda:

- **ORCO**: Guerrero, Cazador, Pícaro, Chamán, Brujo, Caballero de la Muerte
- **NO_MUERTO**: Guerrero, Pícaro, Sacerdote, Mago, Brujo, Caballero de la Muerte
- **TAUREN**: Guerrero, Cazador, Chamán, Druida, Caballero de la Muerte
- **TROLL**: Guerrero, Cazador, Pícaro, Sacerdote, Chamán, Mago, Caballero de la Muerte
- **ELFO_SANGRE**: Guerrero, Paladín, Cazador, Pícaro, Sacerdote, Mago, Brujo, Caballero de la Muerte

## Cómo usar la API

### 1. Consultar razas disponibles con sus clases

```bash
GET http://localhost:8080/api/razas
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "nombre": "HUMANO",
    "faccion": "ALIANZA",
    "clasesDisponibles": [
      {"id": 1, "nombre": "GUERRERO"},
      {"id": 7, "nombre": "PALADIN"},
      {"id": 2, "nombre": "MAGO"},
      {"id": 9, "nombre": "SACERDOTE"},
      {"id": 3, "nombre": "PICARO"},
      {"id": 6, "nombre": "BRUJO"},
      {"id": 11, "nombre": "CABALLERO_DE_LA_MUERTE"}
    ]
  },
  ...
]
```

### 2. Consultar una raza específica

```bash
GET http://localhost:8080/api/razas/8
```

### 3. Crear un jugador

Ahora debes asegurarte de que la combinación de `claseId` y `razaId` sea válida:

```bash
POST http://localhost:8080/api/jugadores
Content-Type: application/json

{
    "nombre": "Mariflor",
    "claseId": 3,
    "razaId": 8,
    "faccionId": 2
}
```

**Nota importante:** 
- Primero consulta `/api/razas` para obtener los IDs correctos de las clases disponibles para cada raza
- El sistema validará automáticamente que la clase elegida sea compatible con la raza

### 4. Ejemplo de error de validación

Si intentas crear un jugador con una combinación inválida (por ejemplo, un Gnomo Druida, que no existe en WoW):

```json
{
    "nombre": "InvalidChar",
    "claseId": 8,  // DRUIDA
    "razaId": 6,   // GNOMO
    "faccionId": 1
}
```

**Respuesta de error:**
```
RuntimeException: La clase DRUIDA no está disponible para la raza GNOMO
```

## Estructura de JugadorRequest

```java
{
    "nombre": "string (requerido)",
    "claseId": "Long (requerido)",
    "razaId": "Long (requerido)",
    "faccionId": "Long (requerido)"
}
```

## Pasos para probar la aplicación

1. **Eliminar la base de datos anterior** (si existe):
   ```bash
   rm -rf ./azeroth_progress_db.*
   ```

2. **Compilar el proyecto**:
   ```bash
   mvn clean compile
   ```

3. **Ejecutar la aplicación**:
   ```bash
   mvn spring-boot:run
   ```

4. **Probar los endpoints**:
   - Consultar razas: `GET http://localhost:8080/api/razas`
   - Crear un jugador válido (ejemplo TROLL + PICARO)
   - Verificar que la validación funciona correctamente

## Resolución del problema original

El problema reportado con TROLL y PICARO estaba relacionado con:

1. **Falta de relación Many-to-Many**: Las razas no tenían definidas sus clases disponibles
2. **Falta de validación**: No se verificaba que la combinación fuera válida
3. **Posible problema de constraint en la BD**: Los checks constraints pueden haber estado mal configurados

Con esta implementación:
- ✅ Cada raza tiene sus clases disponibles definidas en el DataInitializer
- ✅ Se valida la combinación antes de guardar
- ✅ La relación se gestiona mediante tabla intermedia `raza_clase`
- ✅ Se pueden consultar las combinaciones válidas mediante la API

