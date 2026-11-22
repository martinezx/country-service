CREATE TABLE region
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(100)             NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_region_name UNIQUE (name)
);

CREATE TABLE timezone
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    timezone           VARCHAR(32)              NOT NULL,
    utc_offset         VARCHAR(6)               NOT NULL,
    utc_offset_minutes INTEGER                  NOT NULL,
    created_at         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_timezone_timezone UNIQUE (timezone)
);

CREATE TABLE currency
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code       VARCHAR(10)              NOT NULL,
    name       VARCHAR(100)             NOT NULL,
    symbol     VARCHAR(20),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_currency_code UNIQUE (code)
);

CREATE TABLE language
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    iso639_2   VARCHAR(3)               NOT NULL,
    iso639_1   VARCHAR(2),
    name       VARCHAR(100)             NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_language_iso639_2 UNIQUE (iso639_2)
);

CREATE TABLE country
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    alpha2_code  VARCHAR(2)               NOT NULL,
    alpha3_code  VARCHAR(3)               NOT NULL,
    numeric_code VARCHAR(3)               NOT NULL,
    name         VARCHAR(150)             NOT NULL,
    region_id    BIGINT                   NOT NULL,
    latitude     DOUBLE PRECISION,
    longitude    DOUBLE PRECISION,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_country_alpha2_code UNIQUE (alpha2_code),
    CONSTRAINT uq_country_alpha3_code UNIQUE (alpha3_code),
    CONSTRAINT uq_country_numeric_code UNIQUE (numeric_code),
    CONSTRAINT fk_country_region FOREIGN KEY (region_id) REFERENCES region (id)
);

CREATE INDEX idx_country_region_id ON country (region_id);

CREATE TABLE country_currency
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    country_id  BIGINT                   NOT NULL,
    currency_id BIGINT                   NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_country_currency UNIQUE (country_id, currency_id),
    CONSTRAINT fk_country_currency_country FOREIGN KEY (country_id) REFERENCES country (id),
    CONSTRAINT fk_country_currency_currency FOREIGN KEY (currency_id) REFERENCES currency (id)
);

CREATE INDEX idx_country_currency_currency_id ON country_currency (currency_id);

CREATE TABLE country_language
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    country_id  BIGINT                   NOT NULL,
    language_id BIGINT                   NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_country_language UNIQUE (country_id, language_id),
    CONSTRAINT fk_country_language_country FOREIGN KEY (country_id) REFERENCES country (id),
    CONSTRAINT fk_country_language_language FOREIGN KEY (language_id) REFERENCES language (id)
);

CREATE INDEX idx_country_language_language_id ON country_language (language_id);

CREATE TABLE country_timezone
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    country_id  BIGINT                   NOT NULL,
    timezone_id BIGINT                   NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_country_timezone UNIQUE (country_id, timezone_id),
    CONSTRAINT fk_country_timezone_country FOREIGN KEY (country_id) REFERENCES country (id),
    CONSTRAINT fk_country_timezone_timezone FOREIGN KEY (timezone_id) REFERENCES timezone (id)
);

CREATE INDEX idx_country_timezone_timezone_id ON country_timezone (timezone_id);