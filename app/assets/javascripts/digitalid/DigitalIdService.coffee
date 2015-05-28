
class DigitalIdService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserService"

    buildDigitalID: (digitalId, sessionName) ->
        @$log.debug "building digital id"
        deferred = @$q.defer()

        @$http.post("/digitalid/create?sessionName="+sessionName, digitalId)
        .success((data, status, headers) =>
          @$log.info("Successfully built digital id - status #{status}")
          deferred.resolve(data)
        )
        .error((data, status, headers) =>
          @$log.error("Failed to build digital id - status #{status}")
          deferred.reject(data)
        )
        deferred.promise

    listDigitalIDs: () ->
        deferred = @$q.defer()
        @$http.get("/digitalid/list")
        .success((data, status, headers) =>
                @$log.info("Successfully listed digital ids - status #{status}")
                deferred.resolve(data)
        )
        .error((data, status, headers) =>
                @$log.error("Failed to list Users - status #{status}")
                deferred.reject(data)
        )
        deferred.promise

    compareDigitalIDs: (list) ->
       @$log.debug("This is the list" + list)
       deferred = @$q.defer()
       @$http.post("/digitalid/compare", list)
       .success((data, status, headers) =>
             @$log.info("Successfully compared digital ids - status #{status}")
             deferred.resolve(data)
       )
       .error((data, status, headers) =>
             @$log.error("Failed to compare digital ids - status #{status}")
             deferred.reject(data)
       )
       deferred.promise

    openDigitalID: (filename) ->
      @$log.debug("This is the file" + filename)
      deferred = @$q.defer()
      @$http.post("/digitalid/generate", filename)
      .success((data, status, headers) =>
        @$log.info("Successfully downloaded digitalid - status #{status}")
        deferred.resolve(data)
      )
      .error((data, status, headers) =>
        @$log.error("Failed to download digitalid - status #{status}")
        deferred.reject(data)
      )
      deferred.promise

servicesModule.service('DigitalIdService', DigitalIdService)