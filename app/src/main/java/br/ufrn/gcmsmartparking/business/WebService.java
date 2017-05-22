package br.ufrn.gcmsmartparking.business;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.ufrn.gcmsmartparking.annotation.AUTH;
import br.ufrn.gcmsmartparking.annotation.POST;
import br.ufrn.gcmsmartparking.annotation.PUT;
import br.ufrn.gcmsmartparking.annotation.Path;
import br.ufrn.gcmsmartparking.model.User;

/**
 * @author Victor Oliveira
 */
public class WebService {

    private RestTemplate restTemplate = null;
    private ObjectMapper objectMapper = null;
    private String url;

    public User create(User user, Context context) throws Exception {
        ResponseEntity<String> responseEntity = null;
        try {
            url += montarPost(user.getClass());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            String json = getObjectMapperInstance().writeValueAsString(user);
            Log.i("POST URL", url);
            Log.i("JSON POST", json);

            HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
            responseEntity = getRestTemplateInstance().exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (responseEntity.getBody() != null) {
                if (responseEntity.getStatusCode().value() == HttpStatus.CREATED.value() || responseEntity.getStatusCode().ordinal() == HttpStatus.OK.value())
                    return (User) getObjectMapperInstance().readValue(responseEntity.getBody().toString(), user.getClass());
                else
                    throw new Exception(responseEntity.getStatusCode().value() + "," + responseEntity.getBody().toString());
            }
        } catch (Exception e) {
            throw new Exception(responseEntity.getStatusCode().value() + "," + responseEntity.getBody().toString());
        }

        return null;
    }

    public User put(User user, Context context) throws Exception {
        ResponseEntity<String> responseEntity = null;
        try {
            url += montarPut(user.getClass());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            Log.i("PUT URL", url);

            HttpEntity<String> requestEntity = new HttpEntity<String>(getObjectMapperInstance().writeValueAsString(user), headers);
            responseEntity = getRestTemplateInstance().exchange(url, HttpMethod.PUT, requestEntity, String.class);

            if (responseEntity.getBody() != null) {
                if (responseEntity.getStatusCode().value() == HttpStatus.OK.value()) {
                    return (User) getObjectMapperInstance().readValue(responseEntity.getBody().toString(), user.getClass());
                } else {
                    throw new Exception(responseEntity.getStatusCode().value() + "," + responseEntity.getBody().toString());
                }
            }
        } catch (Exception e) {
            throw new Exception(responseEntity.getStatusCode().value() + "," + responseEntity.getBody().toString());
        }

        return null;
    }

    public User auth(User user, Context context) throws Exception {
        ResponseEntity<String> responseEntity = null;
        try {
            url += montarAuth(user.getClass());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            Log.i("AUTH URL", url);

            HttpEntity<String> requestEntity = new HttpEntity<String>(getObjectMapperInstance().writeValueAsString(user), headers);
            responseEntity = getRestTemplateInstance().exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (responseEntity.getBody() != null) {
                if (responseEntity.getStatusCode().value() == HttpStatus.OK.value()) {
                    return (User) getObjectMapperInstance().readValue(responseEntity.getBody().toString(), user.getClass());
                } else {
                    throw new Exception(responseEntity.getStatusCode().value() + "," + responseEntity.getBody().toString());
                }
            }
        } catch (Exception e) {
            throw new Exception(responseEntity.getStatusCode().value() + "," + responseEntity.getBody().toString());
        }

        return null;
    }



    private RestTemplate getRestTemplateInstance() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        }
        return restTemplate;
    }

    private ObjectMapper getObjectMapperInstance() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return objectMapper;
    }

    private String montarPath(Class<?> classEntity) {
        String url = "";

        if (haAnnotation(classEntity, Path.class)) {
            url = this.url.charAt(this.url.length() - 1) == '/' ? getPathName(classEntity) : "/" + getPathName(classEntity);
        } else {
            url = this.url.charAt(this.url.length() - 1) == '/' ? classEntity.getSimpleName() : "/" + classEntity.getSimpleName();
        }
        return url;
    }

    private String montarPost(Class<?> classEntity) {
        String url = "";

        if (haAnnotation(classEntity, POST.class)) {
            url = this.url.charAt(this.url.length() - 1) == '/' ? getPathName(classEntity) : "/" + getPathName(classEntity);
        } else {
            url = this.url.charAt(this.url.length() - 1) == '/' ? classEntity.getSimpleName() : "/" + classEntity.getSimpleName();
        }
        return url;
    }

    private String montarPut(Class<?> classEntity) {
        String url = "";

        if (haAnnotation(classEntity, PUT.class)) {
            url = this.url.charAt(this.url.length() - 1) == '/' ? getPathName(classEntity) : "/" + getPathName(classEntity);
        } else {
            url = this.url.charAt(this.url.length() - 1) == '/' ? classEntity.getSimpleName() : "/" + classEntity.getSimpleName();
        }
        return url;
    }

    private String montarAuth(Class<?> classEntity) {
        String url = "";

        if (haAnnotation(classEntity, AUTH.class)) {
            url = this.url.charAt(this.url.length() - 1) == '/' ? getPathName(classEntity) : "/" + getPathName(classEntity);
        } else {
            url = this.url.charAt(this.url.length() - 1) == '/' ? classEntity.getSimpleName() : "/" + classEntity.getSimpleName();
        }
        return url;
    }

    private String getPathName(Class<?> targetClass) {
        return (targetClass.isAnnotationPresent(Path.class) ? targetClass
                .getAnnotation(Path.class).value() : targetClass.getSimpleName().toLowerCase());
    }

    private String getPostName(Class<?> targetClass) {
        return (targetClass.isAnnotationPresent(POST.class) ? targetClass
                .getAnnotation(POST.class).value() : targetClass.getSimpleName().toLowerCase());
    }

    private String getPutName(Class<?> targetClass) {
        return (targetClass.isAnnotationPresent(PUT.class) ? targetClass
                .getAnnotation(PUT.class).value() : targetClass.getSimpleName().toLowerCase());
    }

    private String getAuthName(Class<?> targetClass) {
        return (targetClass.isAnnotationPresent(AUTH.class) ? targetClass
                .getAnnotation(AUTH.class).value() : targetClass.getSimpleName().toLowerCase());
    }

    private boolean haAnnotation(Class<?> targetClass, Class annotation) {
        return targetClass.isAnnotationPresent(annotation);
    }

}
