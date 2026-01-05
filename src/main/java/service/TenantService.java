package service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import model.ApiKey;
import model.Plan;
import model.Tenant;
import model.User;
import org.springframework.stereotype.Service;
import repository.TenantRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TenantService {
    private final TenantRepository tenantRepository;

    @Transactional
    public Tenant addTenant(Tenant tenant){
        tenantRepository.save(tenant);
        return tenant;
    }

//    @Transactional
//    public Tenant addApiKey(ApiKey apiKey, UUID tenantID){
//        Optional<Tenant> res = tenantRepository.findById(tenantID);
//        if (res.isPresent()) {
//            Tenant tenant = res.get();
//            List<ApiKey> apiKeys = tenant.getApiKeys();
//            apiKeys.add(apiKey);
//            tenantRepository.save(tenant);
//            return tenant;
//        }
//        return null;
//    }

    @Transactional
    public Tenant addUser(User user, UUID tenantID){
        Optional<Tenant> res = tenantRepository.findById(tenantID);
        if (res.isPresent()) {
            Tenant tenant = res.get();
            List<User> users = tenant.getUsers();
            users.add(user);
            tenantRepository.save(tenant);
            return tenant;
        }
        return null;
    }

    @Transactional
    public Tenant updatePlan(Plan plan, UUID tenantID){
        Optional<Tenant> res = tenantRepository.findById(tenantID);
        if (res.isPresent()) {
            Tenant tenant = res.get();
            tenant.setPlan(plan);
            tenantRepository.save(tenant);
            return tenant;
        }
        return null;
    }


}
