CREATE TABLE plans (
                       id UUID PRIMARY KEY,
                       name TEXT NOT NULL,
                       requests_per_minute INT NOT NULL,
                       requests_per_day INT NOT NULL,
                       monthly_quota BIGINT NOT NULL,
                       active BOOLEAN NOT NULL,
                       created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE tenants (
                         id UUID PRIMARY KEY,
                         name TEXT NOT NULL,
                         plan_id UUID NOT NULL,
                         created_at TIMESTAMPTZ NOT NULL,
                         CONSTRAINT fk_tenant_plan
                             FOREIGN KEY (plan_id) REFERENCES plans(id)
);

CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email TEXT NOT NULL UNIQUE,
                       password_hash TEXT NOT NULL,
                       tenant_id UUID NOT NULL,
                       created_at TIMESTAMPTZ NOT NULL,
                       CONSTRAINT fk_user_tenant
                           FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

CREATE TABLE api_keys (
                          id UUID PRIMARY KEY,
                          key_hash TEXT NOT NULL,
                          tenant_id UUID NOT NULL,
                          revoked BOOLEAN NOT NULL,
                          created_at TIMESTAMPTZ NOT NULL,
                          expires_at TIMESTAMPTZ,
                          CONSTRAINT fk_api_key_tenant
                              FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

CREATE INDEX idx_tenants_plan_id ON tenants(plan_id);
CREATE INDEX idx_users_tenant_id ON users(tenant_id);
CREATE INDEX idx_api_keys_tenant_id ON api_keys(tenant_id);
CREATE INDEX idx_api_keys_key_hash ON api_keys(key_hash);
