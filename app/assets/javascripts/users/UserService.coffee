
class UserService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserService"

    listUsers: () ->
        @$log.debug "listUsers()"
        deferred = @$q.defer()

        @$http.get("/users")
        .success((data, status, headers) =>
                @$log.info("Successfully listed Users - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Users - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    createUser: (user) ->
        @$log.debug "createUser #{angular.toJson(user, true)}"
        deferred = @$q.defer()

        @$http.post('/user', user)
        .success((data, status, headers) =>
                @$log.info("Successfully created User - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create user - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    updateUser: (firstName, lastName, user) ->
        @$log.debug "updateUser #{angular.toJson(user, true)}"
        deferred = @$q.defer()

        @$http.put("/user/#{firstName}/#{lastName}", user)
        .success((data, status, headers) =>
                @$log.info("Successfully updated User - status #{status}")
                deferred.resolve(data)
              )
        .error((data, status, header) =>
                @$log.error("Failed to update user - status #{status}")
                deferred.reject(data)
              )
        deferred.promise

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

servicesModule.service('UserService', UserService)