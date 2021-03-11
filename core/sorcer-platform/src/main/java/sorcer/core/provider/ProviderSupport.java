/*
 * Copyright 2018 the original author or authors.
 * Copyright 2018 SorcerSoft.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sorcer.core.provider;

import sorcer.service.Routine;

import java.util.List;

/**
 * Methods delegated to service beans by service providers
 *
 * @see ServiceExerter
 *
 * @author Mike Sobolewski
 */
public interface ProviderSupport {

	List<String> getAvailableApps();

	boolean isReady(Routine exertion);
}
