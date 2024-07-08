Feature: Weather API

  Scenario Outline: Получение текущей погоды для определённого города
    Given ключ API для погоды
    When я запрашиваю текущую погоду для <city>
    Then ответ содержит Status Code 200
    And ответ содержит ожидаемые данные о погоде для <city>

    Examples:
      | city     |
      | Tyumen   |
      | Alicante |
      | Moscow   |
      | Ushuaia  |

  Scenario: Получение ошибки при запросе погоды несуществующего города
    Given ключ API для погоды
    When я запрашиваю текущую погоду для SberbankCity
    Then ответ содержит Status Code 400
    And ответ содержит Error Code 1006

  Scenario: Получение ошибки при запросе погоды с несуществующим токеном
    Given несуществующий ключ API для погоды
    When я запрашиваю текущую погоду для Soda Springs
    Then ответ содержит Status Code 403
    And ответ содержит Error Code 2008

  Scenario: Получение ошибки при запросе погоды без токена
    When я запрашиваю без токена текущую погоду для Soda Springs
    Then ответ содержит Status Code 401
    And ответ содержит Error Code 1002

  Scenario: Получение ошибки при запросе погоды без города
    Given ключ API для погоды
    When я запрашиваю текущую погоду без города
    Then ответ содержит Status Code 400
    And ответ содержит Error Code 1003