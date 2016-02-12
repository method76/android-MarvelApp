package com.method76.comics.marvel.common.constant;

/**
 * Created by Sungjoon Kim on 2016-02-05.
 */
public interface MarvelApiError {

    enum MarvelError{
//        409 Missing API Key	Occurs when the apikey parameter is not included with a request.
//        409	Missing Hash	Occurs when an apikey parameter is included with a request, a ts parameter is present, but no hash parameter is sent. Occurs on server-side applications only.
//        409	Missing Timestamp	Occurs when an apikey parameter is included with a request, a hash parameter is present, but no ts parameter is sent. Occurs on server-side applications only.
//        401	Invalid Referer	Occurs when a referrer which is not valid for the passed apikey parameter is sent.
//        401	Invalid Hash	Occurs when a ts, hash and apikey parameter are sent but the hash is not valid per the above hash generation rule.
//        405	Method Not Allowed	Occurs when an API endpoint is accessed using an HTTP verb which is not allowed for that endpoint.
//        403	Forbidden	Occurs when a user with an otherwise authenticated request attempts to access an endpoint to which they do not have access.

    }

}
