package tpbeatiniortega

class BootStrap {

    def init = { servletContext ->
        def userInstance = new User(username: "username",
                password: "password",
                thumbnail: new Illustration(filename: "image.png")).save(flush: true, failOnError: true)
        (1..5).each{
            userInstance.addToAnnonces(
                    new Annonce(
                        title: "Title"+it,
                        description: "description",
                        validTill: new Date(),
                        state: Boolean.TRUE
            )
                            .addToIllustrations(new Illustration(filename: "filename.png"))
                            .addToIllustrations(new Illustration(filename: "filename2.png"))
                            .addToIllustrations(new Illustration(filename: "filename3.png"))
            )
        }
        userInstance.save(flush:true, failOnError:true)
    }
    def destroy = {
    }
}
