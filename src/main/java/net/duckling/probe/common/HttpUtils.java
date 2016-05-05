/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.probe.common;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtils {
	private static TrustManager easyTrustManager = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// All Client Certificate is allowed.
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// All Server Certificate is allowed.
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	public static URLConnection createUrlconnection(String urlString)
			throws IOException {
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		if (conn instanceof HttpsURLConnection) {
			TrustManager[] tm = { easyTrustManager };
			SSLContext sslContext;
			try {
				sslContext = SSLContext.getInstance("SSL", "SunJSSE");
				sslContext.init(null, tm, new java.security.SecureRandom());
				// 从上述SSLContext对象中得到SSLSocketFactory对象
				SSLSocketFactory ssf = sslContext.getSocketFactory();
				HttpsURLConnection httpURLConnection = (HttpsURLConnection) conn;
				httpURLConnection.setSSLSocketFactory(ssf);
				httpURLConnection.setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession sslSession) {
						return true;
					}
				});
			} catch (NoSuchAlgorithmException | NoSuchProviderException
					| KeyManagementException e) {
				throw new IOException("Create Https failed.", e);
			}
		}
		return conn;
	}
}
