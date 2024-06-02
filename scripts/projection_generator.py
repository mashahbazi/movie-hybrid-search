import random

original_dim = 4096
reduced_dim = 1999
seed_value = 42

# Set the seed for reproducibility
random.seed(seed_value)

# Create the projection matrix with random Gaussian values
projection_matrix = [[random.gauss(0, 1) for _ in range(original_dim)] for _ in range(reduced_dim)]

# Write the projection matrix to a file
with open("src/main/resources/projection_matrix.txt", "w") as out_file:
    for row in projection_matrix:
        out_file.write(" ".join(map(str, row)) + "\n")