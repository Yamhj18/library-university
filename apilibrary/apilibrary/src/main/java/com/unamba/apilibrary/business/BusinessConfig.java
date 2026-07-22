package com.unamba.apilibrary.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unamba.apilibrary.dto.request.RequestConfigUpdate;
import com.unamba.apilibrary.dto.response.ResponseConfigGetAll;
import com.unamba.apilibrary.dto.response.ResponseGenericMessage;
import com.unamba.apilibrary.entity.EntitySystemConfig;
import com.unamba.apilibrary.repository.RepositorySystemConfig;

@Service
public class BusinessConfig {

    private final RepositorySystemConfig repositorySystemConfig;

    public BusinessConfig(RepositorySystemConfig repositorySystemConfig) {
        this.repositorySystemConfig = repositorySystemConfig;
    }

    public ResponseConfigGetAll getAll() {
        ResponseConfigGetAll response = new ResponseConfigGetAll();

        List<EntitySystemConfig> list = repositorySystemConfig.findAll();

        list.forEach(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idConfig", c.getIdConfig());
            map.put("configKey", c.getConfigKey());
            map.put("configValue", c.getConfigValue());
            map.put("description", c.getDescription());
            response.getListConfig().add(map);
        });

        response.success();
        return response;
    }

    public ResponseGenericMessage update(RequestConfigUpdate request) {
        ResponseGenericMessage response = new ResponseGenericMessage();

        request.getConfigs().forEach((key, value) -> {
            EntitySystemConfig config = repositorySystemConfig.findByConfigKey(key).orElse(null);
            if (config != null) {
                config.setConfigValue(value);
                repositorySystemConfig.save(config);
            }
        });

        response.success();
        response.listMessage.add("Configuración actualizada exitosamente.");
        return response;
    }
}
