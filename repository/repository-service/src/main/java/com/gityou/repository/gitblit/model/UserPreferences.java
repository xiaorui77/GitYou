/*
 * Copyright 2013 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gityou.repository.gitblit.model;

import com.gityou.repository.gitblit.Constants.Transport;
import com.gityou.repository.gitblit.utils.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * User preferences.
 *
 * @author James Moger
 */
public class UserPreferences implements Serializable {

    private static final long serialVersionUID = 1L;

    public final String username;

    private String locale;

    private Boolean emailMeOnMyTicketChanges;

    private Transport transport;

    private final Map<String, UserRepositoryPreferences> repositoryPreferences = new TreeMap<String, UserRepositoryPreferences>();

    public UserPreferences(String username) {
        this.username = username;
    }

    public Locale getLocale() {
        if (StringUtils.isEmpty(locale)) {
            return null;
        }
        int underscore = locale.indexOf('_');
        if (underscore > 0) {
            String lang = locale.substring(0, underscore);
            String cc = locale.substring(underscore + 1);
            return new Locale(lang, cc);
        }
        return new Locale(locale);
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public UserRepositoryPreferences getRepositoryPreferences(String repositoryName) {
        String key = repositoryName.toLowerCase();
        if (!repositoryPreferences.containsKey(key)) {
            // default preferences
            UserRepositoryPreferences prefs = new UserRepositoryPreferences();
            prefs.username = username;
            prefs.repositoryName = repositoryName;
            repositoryPreferences.put(key, prefs);
        }
        return repositoryPreferences.get(key);
    }

    public void setRepositoryPreferences(UserRepositoryPreferences pref) {
        repositoryPreferences.put(pref.repositoryName.toLowerCase(), pref);
    }

    public boolean isStarredRepository(String repository) {
        if (repositoryPreferences == null) {
            return false;
        }
        String key = repository.toLowerCase();
        if (repositoryPreferences.containsKey(key)) {
            UserRepositoryPreferences pref = repositoryPreferences.get(key);
            return pref.starred;
        }
        return false;
    }

    public List<String> getStarredRepositories() {
        List<String> list = new ArrayList<String>();
        for (UserRepositoryPreferences prefs : repositoryPreferences.values()) {
            if (prefs.starred) {
                list.add(prefs.repositoryName);
            }
        }
        Collections.sort(list);
        return list;
    }

    public boolean isEmailMeOnMyTicketChanges() {
        if (emailMeOnMyTicketChanges == null) {
            return true;
        }
        return emailMeOnMyTicketChanges;
    }

    public void setEmailMeOnMyTicketChanges(boolean value) {
        this.emailMeOnMyTicketChanges = value;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
